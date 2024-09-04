package me.goodt.nsi

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository

interface AbstractNsiRepository<T, ID> : CrudRepository<T, ID>, JpaSpecificationExecutor<T>
