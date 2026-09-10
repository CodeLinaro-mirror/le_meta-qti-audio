inherit autotools-brokensep pkgconfig qprebuilt

HOMEPAGE         = "http://support.cdmatech.com"
LICENSE          = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

DESCRIPTION = "Audio MCS"
PR = "r5"

SRC_DIR     =  "${WORKSPACE}/vendor/qcom/opensource/audio_mcs/mcs/"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://vendor/qcom/opensource/audio_mcs/mcs/"

S = "${WORKDIR}/vendor/qcom/opensource/audio_mcs/mcs"

DEPENDS = "glib-2.0 agm audio-route mm-audio-headers"
EXTRA_OECONF += "--with-glib"

PR = "r0"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""


FILES:${PN}-dbg  = "${libdir}/.debug/* ${bindir}/.debug/*"
FILES:${PN}      = "${libdir}/*.so ${libdir}/*.so.* ${sysconfdir}/* ${bindir}/* ${libdir}/pkgconfig/*"
FILES:${PN}-dev  = "${libdir}/*.la ${includedir}"
