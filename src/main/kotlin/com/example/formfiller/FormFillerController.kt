package com.example.formfiller

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FormFillerController(private val formFillerService: FormFillerService) {
    @GetMapping("api/v1/nda")
    fun fillForm(
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) name2: String? = null,
        @RequestParam(required = false) name3: String? = null,
        @RequestParam(required = false) address: String? = null,
        @RequestParam(required = false) address2: String? = null,
        @RequestParam(required = false) description: String? = null,
    ): ResponseEntity<ByteArray> {
        val bytes = formFillerService.fillForm(
            name = name,
            name2 = name2,
            name3 = name3,
            address = address,
            address2 = address2,
            description = description,
        )
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=NDA.pdf")
            .body(bytes)
    }
}