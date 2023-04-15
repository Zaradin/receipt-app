package persistence

/**
 * Interface for classes that can read and write objects from and to a persistent storage.
 * @auther Josh Crotty
 * @since 2.0
 */
interface Serializer {
    /**
     * Writes an object to a persistent storage.
     *
     * @param obj the object to write.
     * @throws Exception if there was an error writing the object to the storage.
     */
    @Throws(Exception::class)
    fun write(obj: Any?)

    /**
     * Reads an object from a persistent storage.
     *
     * @return the object read from the storage.
     * @throws Exception if there was an error reading the object from the storage.
     */
    @Throws(Exception::class)
    fun read(): Any?
}
