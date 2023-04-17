inherit autotools pkgconfig

DESCRIPTION = "Tinyalsa Library"
LICENSE = "BSD-Source-Code"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=fe8b41221d7524c70688f7d059ff6d87"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinyalsa/"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/tinyalsa/"
PR = "r0"
PV = "1.0.c9"

DEPENDS = "libcutils glib-2.0"

EXTRA_OECONF += "--with-glib"

SOLIBS = ".so"

FILES_SOLIBSDEV = ""
