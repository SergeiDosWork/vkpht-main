package me.goodt.nsi

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
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
import java.util.stream.Collectors

abstract class AbstractNsiController<T, ID : Any> {
    private val repository: AbstractNsiRepository<T?, ID?>
    private val assembler: RepresentationModelAssembler<T?, EntityModel<T>>
    private val metaGenerator: DictionaryMetaGenerator

    constructor(repository: AbstractNsiRepository<T?, ID?>, assembler: RepresentationModelAssembler<T?, EntityModel<T>>, metaGenerator: DictionaryMetaGenerator) {
        this.repository = repository
        this.assembler = assembler
        this.metaGenerator = metaGenerator
    }


    @GetMapping("/{id}")
    fun one(@PathVariable id: ID): EntityModel<T> = repository.findById(id).orElseThrow { ResourceNotFoundException(id) }.let(assembler::toModel as (T?) -> EntityModel<T>)

    @PostMapping
    fun newEntity(@RequestBody newEntity: T): ResponseEntity<*> {
        val entityModel = assembler.toModel(repository.save(newEntity))
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
    }

    @PutMapping("/{id}")
    fun replaceEntity(@RequestBody newEntity: T, @PathVariable id: ID): ResponseEntity<*> = repository.findById(id).map { updateEntity(it, newEntity); it }.orElseGet { setId(newEntity, id); newEntity }.let { repository.save(it) }.let { assembler.toModel(it) }.let { ResponseEntity.created(it.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(it) }


    @DeleteMapping("/{id}")
    fun deleteEntity(@PathVariable id: ID): ResponseEntity<*> {
        repository.deleteById(id)
        return ResponseEntity.noContent().build<Any>()
    }

    @GetMapping
    fun all(@RequestParam filter: Filter?, @RequestParam page: Optional<Int?>, @RequestParam size: Optional<Int?>, @RequestParam sortBy: Optional<String?>): PagedModel<EntityModel<T>> {
        val spec = createSpecification(filter)
        val pageable: Pageable = PageRequest.of(page.orElse(0)!!, size.orElse(10)!!, Sort.by(sortBy.orElse("id")).ascending())
        val pageResult = repository.findAll(spec, pageable)
        val entities = pageResult.content.stream().map { entity: T? -> assembler.toModel(entity) }.collect(Collectors.toList())
        return PagedModel.of(entities, PagedModel.PageMetadata(pageResult.size.toLong(), pageResult.number.toLong(), pageResult.totalElements))
    }

    @GetMapping("/count")
    fun count(@RequestParam filter: Filter?): ResponseEntity<Long> {
        val count = repository.count(createSpecification(filter))
        return ResponseEntity.ok(count)
    }

    @GetMapping("/meta")
    fun metadata(): ResponseEntity<DictionaryMetaResponse> =
            ResponseEntity.ok(buildMetaResponse().apply {
                relatedLinks.forEach { this.add(it) }
            })


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

    fun createSpecification(filter: Filter?): Specification<T?> =
            Specification { root, query, criteriaBuilder ->
                when (filter) {
                    is FilterCondition -> {
                        val field = filter.field
                        val value = filter.value
                        val operator = filter.operator
                        when (operator?.toLowerCase()) {
                            "like" -> criteriaBuilder.like(root.get(field), "%${value?:""}%")
                            "eq" -> criteriaBuilder.equal(root.get<Any>(field), value)
                            "neq" -> criteriaBuilder.notEqual(root.get<Any>(field), value)
                            "lt" -> criteriaBuilder.lt(root.get(field), value?.toBigDecimal())
                            "gt" -> criteriaBuilder.gt(root.get(field), value?.toBigDecimal())
                            // TODO другие операторы?
                            else -> null
                        }
                    }
                    is FilterGroup -> {
                        val group = filter
                        val predicates = group.conditions.map { createSpecification(it).toPredicate(root, query, criteriaBuilder) }
                        when (group.operator?.toLowerCase()) {
                            "and" -> criteriaBuilder.and(*predicates.toTypedArray())
                            "or" -> criteriaBuilder.or(*predicates.toTypedArray())
                            else -> null
                        }
                    }
                    else -> null
                }
            }

    protected abstract fun updateEntity(existingEntity: T?, newEntity: T)
    protected abstract fun setId(entity: T, id: ID)
    protected abstract val relatedLinks: Collection<Link>
}
