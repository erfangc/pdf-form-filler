package com.example.formfiller

import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.interactive.form.PDField
import java.io.File
import java.io.FileInputStream

fun main() {
    val file = File("ImageFillerSample.pdf")
    val document = PDDocument.load(file)

    val acroForm = document.documentCatalog.acroForm
    val pdField = acroForm.getField("Text2")

    val image = FileInputStream("picture.jpeg").readAllBytes()

    val pdImageXObject = PDImageXObject.createFromByteArray(document, image, "something")

    if (pdField != null) {
        val rectangle = getFieldArea(pdField)
        val height = rectangle.height
        val width = rectangle.width
        val x = rectangle.lowerLeftX
        val y = rectangle.lowerLeftY

        val contentStream = PDPageContentStream(
            document,
            document.getPage(0),
            PDPageContentStream.AppendMode.APPEND,
            true
        )

        contentStream.drawImage(
            pdImageXObject,
            x,
            y,
            width,
            height
        )

        contentStream.close()

        document.save("Output.pdf")
        document.close()
    }
}

private fun getFieldArea(field: PDField): PDRectangle {
    val fieldDict = field.cosObject
    val fieldAreaArray = fieldDict.getDictionaryObject(COSName.RECT)
    return PDRectangle(fieldAreaArray as COSArray)
}
