package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Product
import models.Receipt
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Serializer for JSON files.
 * @auther Josh Crotty
 * @since 2.0
 * @property file the file to be read from or written to.
 */
class JSONSerializer(private val file: File) : Serializer {

    /**
     * Read an object from a JSON file.
     *
     * @throws Exception if there is an error reading the object.
     * @return the object read from the file.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(Receipt::class.java))
        xStream.allowTypes(arrayOf(Product::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Write an object to a JSON file.
     *
     * @throws Exception if there is an error writing the object.
     * @param obj the object to be written.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
