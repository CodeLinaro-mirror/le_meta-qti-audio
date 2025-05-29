inherit autotools pkgconfig

DESCRIPTION = "Tinycompress Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinycompress"

S = "${WORKDIR}/vendor/qcom/opensource/tinycompress"
PR = "r0"

DEPENDS = "virtual/kernel glib-2.0"

EXTRA_OECONF += "--with-glib"

SOLIBS = ".so"

FILES_SOLIBSDEV = ""
