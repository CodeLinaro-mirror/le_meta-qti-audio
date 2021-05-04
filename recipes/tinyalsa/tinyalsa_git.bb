inherit autotools pkgconfig

DESCRIPTION = "Tinyalsa Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI = "file://vendor/qcom/opensource/tinyalsa"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/tinyalsa"
PR = "r0"

DEPENDS = "libcutils expat glib-2.0 "

EXTRA_OECONF = "--with-glib"


SOLIBS = ".so"

FILES_SOLIBSDEV = ""

RM_WORK_EXCLUDE += "${PN}"
