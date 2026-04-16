inherit autotools pkgconfig

DESCRIPTION = "Tinycompress Library"
LICENSE = "BSD-3-Clause & LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://NOTICE;md5=862096b6f5c1999f0a0fe356f0907cf6"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI += "file://vendor/qcom/opensource/tinycompress"

S = "${WORKDIR}/vendor/qcom/opensource/tinycompress"
PR = "r0"

DEPENDS = "virtual/kernel glib-2.0"

EXTRA_OECONF += "--with-glib"

SOLIBS = ".so"

FILES_SOLIBSDEV = ""
