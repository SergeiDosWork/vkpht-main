package me.goodt.nsi

import lombok.RequiredArgsConstructor
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.hateoas.*
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

abstract class AbstractNsiController<T, ID : Any> {
    private val repository: AbstractNsiRepository<T?, ID?>
    private val assembler: RepresentationModelAssembler<T?, EntityModel<T>>
    private val metaGenerator: DictionaryMetaGenerator

    constructor(repository: AbstractNsiRepository<T?, ID?>,
                assembler: RepresentationModelAssembler<T?, EntityModel<T>>,
                metaGenerator: DictionaryMetaGenerator) {
        this.repository = repository
        this.assembler = assembler
        this.metaGenerator = metaGenerator
    }


    @GetMapping
    fun all(
            @RequestParam filter: Filter?,
            @RequestParam page: Optional<Int?>,
            @RequestParam size: Optional<Int?>,
            @RequestParam sortBy: Optional<String?>): PagedModel<EntityModel<T>> {
        val spec = createSpecification(filter)
        val pageable: Pageable = PageRequest.of(
                page.orElse(0)!!,
                size.orElse(10)!!,
                Sort.by(sortBy.orElse("id")).ascending()
        )
        val pageResult = repository.findAll(spec, pageable)
        val entities = pageResult.content.stream()
                .map { entity: T? -> assembler.toModel(entity) }
                .collect(Collectors.toList())
        return PagedModel.of(entities, PagedModel.PageMetadata(
                pageResult.size.toLong(),
                pageResult.number.toLong(),
                pageResult.totalElements
        ))
    }

    @GetMapping("/{id}")
    fun one(@PathVariable id: ID): EntityModel<T> {
        val entity = repository.findById(id)
                .orElseThrow { ResourceNotFoundException(id) }
        return assembler.toModel(entity)
    }

    @PostMapping
    fun newEntity(@RequestBody newEntity: T): ResponseEntity<*> {
        val entityModel = assembler.toModel(repository.save(newEntity))
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel)
    }

    @PutMapping("/{id}")
    fun replaceEntity(@RequestBody newEntity: T, @PathVariable id: ID): ResponseEntity<*> {
        val updatedEntity = repository.findById(id)
                .map { entity: T? ->
                    updateEntity(entity, newEntity)
                    repository.save(entity)
                }
                .orElseGet {
                    setId(newEntity, id)
                    repository.save(newEntity)
                }
        val entityModel = assembler.toModel(updatedEntity)
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel)
    }

    @DeleteMapping("/{id}")
    fun deleteEntity(@PathVariable id: ID): ResponseEntity<*> {
        repository.deleteById(id)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping("/count")
    fun count(@RequestParam filter: Filter?): ResponseEntity<Long> {
        val count = repository.count(createSpecification(filter))
        return ResponseEntity.ok(count)
    }

    @GetMapping("/meta")
    fun metadata(): ResponseEntity<DictionaryMetaResponse> {
        val response = buildMetaResponse()
        relatedLinks.forEach(Consumer { link: Link? -> response.add(link) })
        return ResponseEntity.ok(response)
    }

    protected fun buildMetaResponse(): DictionaryMetaResponse {
        val dtoType = dtoClass
        val response = DictionaryMetaResponse()
        response.dto = metaGenerator.generate(dtoType)
        return response
    }

    protected val dtoClass: Class<T>
        /**
         * Возвращает класс обрабатываемого объекта записи (DTO), используемый в мета-модели.
         *
         *
         * Базовая реализация основывается на рефлексии и извлечении параметров интерфейса [DictController].
         * Наследники могут переопределить метод, чтобы возвращать другой класс.
         */
        protected get() {
            val controllerClass = javaClass.genericSuperclass as ParameterizedType
            return controllerClass.actualTypeArguments[1] as Class<T>
        }

    protected abstract fun updateEntity(existingEntity: T?, newEntity: T)
    protected abstract fun setId(entity: T, id: ID)
    protected abstract fun createSpecification(filter: Filter?): Specification<T?>
    protected abstract val relatedLinks: Collection<Link?>
}
