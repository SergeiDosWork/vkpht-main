package me.goodt.nsi

class ResourceNotFoundException(id: Any) : RuntimeException("Resource not found: $id")
