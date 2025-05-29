inherit autotools pkgconfig

DESCRIPTION = "Tinyalsa Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinyalsa"

S = "${WORKDIR}/vendor/qcom/opensource/tinyalsa"
PR = "r0"

DEPENDS = "libcutils glib-2.0 "

EXTRA_OECONF += "--with-glib"


SOLIBS = ".so"

FILES_SOLIBSDEV = ""
