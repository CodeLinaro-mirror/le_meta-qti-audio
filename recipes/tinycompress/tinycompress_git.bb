inherit autotools pkgconfig

DESCRIPTION = "Tinycompress Library"
LICENSE = "BSD & LGPLv2.1"
LIC_FILES_CHKSUM = "file://NOTICE;md5=862096b6f5c1999f0a0fe356f0907cf6"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinycompress/"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/tinycompress/"
PR = "r0"
PV = "1.0"

DEPENDS = "linux-msm-headers glib-2.0"

EXTRA_OECONF += "--with-glib"

SOLIBS = ".so"

FILES_SOLIBSDEV = ""
