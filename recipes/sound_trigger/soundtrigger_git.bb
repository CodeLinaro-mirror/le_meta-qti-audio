inherit autotools-brokensep pkgconfig

DESCRIPTION      = "sound trigger loader Library"
LICENSE          = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r9"

SRC_DIR     =  "${WORKSPACE}/audio/mm-audio/st-hal"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://audio/mm-audio/st-hal/"

S = "${WORKDIR}/audio/mm-audio/st-hal"

do_fetch_extra () {
  cp -rf ${COREBASE}/meta-qti-audio/recipes/sound_trigger/mixers/ ${WORKDIR}
}
addtask do_fetch_extra before do_fetch

DEPENDS = "tinyalsa expat libcutils tinycompress media-headers audio-route libhardware acdbloader graphite-client"
DEPENDS_append = "${@bb.utils.contains('DISTRO_FEATURES', 'audio-dlkm', ' audiodlkm', '', d)}"
DEPENDS_append_apq8017 = " ffv"
DEPENDS_append_apq8009 = " ffv esp"

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += "--with-glib"
EXTRA_OECONF += "BOARD_SUPPORTS_SOUND_TRIGGER_TRANSITION=false"
EXTRA_OECONF += "BUILD_APP=false"
EXTRA_OECONF += "BOARD_SUPPORTS_QSTHW_API=true"
EXTRA_OECONF += "--with-hal-path=${libdir}"
EXTRA_OECONF_append_apq8017 = " BOARD_SUPPORTS_SOUND_TRIGGER_ARM=true"
EXTRA_OECONF_append_apq8009 = " BOARD_SUPPORTS_SOUND_TRIGGER_ARM=true"
EXTRA_OECONF_append_apq8009 = " ENABLE_KEEP_ALIVE=true"
EXTRA_OECONF_append_apq8009 = " BOARD_SUPPORTS_SOUND_TRIGGER_CPU_AFFINITY_SET=true"
EXTRA_OECONF_append_kona = " --enable-feature_sva_multistage"
EXTRA_OECONF_append_qcs40x = " --enable-deferred_stop"
EXTRA_OECONF_append_qcs40x = " --enable-feature_sva_multistage"

EXTRA_OEMAKE = "DEFAULT_INCLUDES= CPPFLAGS="-I. -I${STAGING_KERNEL_BUILDDIR}/usr/include -I${STAGING_INCDIR}/graphite-client/gcs -I${STAGING_INCDIR}/graphite-client/osal""

do_install_append () {
  if [ -d "${WORKDIR}/mixers/${BASEMACHINE}" ] && [ $(ls -1 ${WORKDIR}/mixers/${BASEMACHINE} | wc -l) -ne 0 ]; then
    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/mixers/${BASEMACHINE}/* ${D}${sysconfdir}/
  fi
}

FILES_${PN}     += "${libdir}/sound_trigger.primary.default.so"
