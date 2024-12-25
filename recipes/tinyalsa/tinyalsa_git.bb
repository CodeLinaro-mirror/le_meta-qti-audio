inherit autotools pkgconfig

DESCRIPTION = "Tinyalsa Library"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=7a434440b651f4a472ca93716d01033a"
FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinyalsa"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/tinyalsa"
PR = "r0"

DEPENDS = "libcutils glib-2.0 "

EXTRA_OECONF += "--with-glib"


SOLIBS = ".so"

FILES_SOLIBSDEV = ""
