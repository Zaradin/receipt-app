package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Product
import models.Receipt
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import kotlin.Throws

/**
 * Implementation of Serializer interface that uses XML serialization.
 * @auther Josh Crotty
 * @since 2.0
 * @param file the file to read/write from/to
 */
class XMLSerializer(private val file: File) : Serializer {

    /**
     * Reads an object from an XML file.
     *
     * @return the deserialized object
     * @throws Exception if an error occurs during deserialization
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Receipt::class.java))
        xStream.allowTypes(arrayOf(Product::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes an object to an XML file.
     *
     * @param obj the object to write
     * @throws Exception if an error occurs during serialization
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(DomDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
