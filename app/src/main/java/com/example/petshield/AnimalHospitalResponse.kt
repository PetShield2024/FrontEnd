package com.example.petshield
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

@Root(name = "Animalhosptl", strict = false)
data class AnimalHospitalResponse(
    @field:Element(name = "head") var head: Head? = null,
    @field:ElementList(name = "row", inline = true) var row: List<AnimalHospital>? = null
)

@Root(name = "head", strict = false)
data class Head(
    @field:Element(name = "list_total_count") var listTotalCount: Int? = null,
    @field:Element(name = "RESULT") var result: Result? = null
)

@Root(name = "RESULT", strict = false)
data class Result(
    @field:Element(name = "CODE") var code: String? = null,
    @field:Element(name = "MESSAGE") var message: String? = null
)

@Root(name = "row", strict = false)
data class AnimalHospital(
    @field:Element(name = "SIGUN_NM", required = false) var sigunName: String? = null,
    @field:Element(name = "BIZPLC_NM", required = false) var bizplcName: String? = null,
    @field:Element(name = "LOCPLC_FACLT_TELNO", required = false) var telNo: String? = null,
    @field:Element(name = "REFINE_ROADNM_ADDR", required = false) var lotnoAddr: String? = null,
    @field:Element(name = "REFINE_WGS84_LOGT", required = false) var refineWgs84Logt: Double? = 0.0,
    @field:Element(name = "REFINE_WGS84_LAT", required = false) var refineWgs84Lat: Double? = 0.0
)
