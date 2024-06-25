inherit autotools pkgconfig

DESCRIPTION = "Tinycompress Library"
LICENSE = "${@bb.utils.contains('LAYERSERIES_COMPAT_core', 'dunfell',\
           'BSD & LGPLv2.1','BSD & LGPL-2.1-only', d)}"
LIC_FILES_CHKSUM = "file://NOTICE;md5=862096b6f5c1999f0a0fe356f0907cf6"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinycompress"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/tinycompress"
PR = "r0"

DEPENDS = "virtual/kernel libcutils glib-2.0"

EXTRA_OECONF += "--with-glib"

SOLIBS = ".so"

FILES_SOLIBSDEV = ""
