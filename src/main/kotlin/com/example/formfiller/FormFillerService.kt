package com.example.formfiller

import org.apache.pdfbox.pdmodel.PDDocument
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File

@Service
class FormFillerService {

    private val log = LoggerFactory.getLogger(FormFillerService::class.java)
    val inputFile = File("NDA-Template.pdf")

    fun fillForm(
        name: String? = null,
        name2: String? = null,
        name3: String? = null,
        address: String? = null,
        address2: String? = null,
        description: String? = null,
    ): ByteArray {

        val answers = mapOf(
            "name" to name,
            "name2" to name2,
            "name3" to name3,
            "address" to address,
            "address2" to address2,
            "description" to description,  
        )
        
        val pdfDocument = PDDocument.load(inputFile)
        val documentCatalog = pdfDocument.documentCatalog
        val acroForm = documentCatalog.acroForm

        if (acroForm != null) {
            val fields = acroForm.fields
            for (field in fields) {
                field.setValue(answers[field.fullyQualifiedName.lowercase()])
            }
            val outputStream = ByteArrayOutputStream()
            pdfDocument.save(outputStream)
            log.info("Filled out form with answers=$answers")
            pdfDocument.close()
            return outputStream.toByteArray()
        } else {
            error("...")
        }

    }

}