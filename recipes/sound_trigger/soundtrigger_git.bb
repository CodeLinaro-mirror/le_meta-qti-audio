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

DEPENDS = "tinyalsa expat libcutils tinycompress media-headers audio-route libhardware acdbloader graphite-client audiohal"
DEPENDS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'audio-dlkm', ' audiodlkm', '', d)}"
DEPENDS:append:apq8017 = " ffv"
DEPENDS:append:apq8009 = " ffv esp"

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += "--with-glib"
EXTRA_OECONF += "BOARD_SUPPORTS_SOUND_TRIGGER_TRANSITION=false"
EXTRA_OECONF += "BUILD_APP=false"
EXTRA_OECONF += "BOARD_SUPPORTS_QSTHW_API=true"
EXTRA_OECONF += "--with-hal-path=${libdir}"
EXTRA_OECONF:append:apq8017 = " BOARD_SUPPORTS_SOUND_TRIGGER_ARM=true"
EXTRA_OECONF:append:apq8009 = " BOARD_SUPPORTS_SOUND_TRIGGER_ARM=true"
EXTRA_OECONF:append:apq8009 = " ENABLE_KEEP_ALIVE=true"
EXTRA_OECONF:append:apq8009 = " BOARD_SUPPORTS_SOUND_TRIGGER_CPU_AFFINITY_SET=true"
EXTRA_OECONF:append:kona = " --enable-feature_sva_multistage"
EXTRA_OECONF:append:kona = " SUPPORTS_SOUND_TRIGGER_DEVICE_API_VERSION_1_0=true"
EXTRA_OECONF:append:qrbx210 = " --enable-feature_sva_multistage"
EXTRA_OECONF:append:qrbx210 = " SUPPORTS_SOUND_TRIGGER_DEVICE_API_VERSION_1_0=true"
EXTRA_OECONF:append:qcs40x = " --enable-deferred_stop"
EXTRA_OECONF:append:qcs40x = " --enable-feature_sva_multistage"
EXTRA_OECONF:append:sdmsteppe = " --enable-feature_sva_multistage"
EXTRA_OECONF:append:sdmsteppe = " SUPPORTS_SOUND_TRIGGER_APE=true"
EXTRA_OECONF:append:qrb5165 = " AUDIO_FEATURE_ENABLED_AUDIO_LEGACY_TECHPACK=true"

EXTRA_OEMAKE = "DEFAULT_INCLUDES= CPPFLAGS="-I. -I${STAGING_KERNEL_BUILDDIR}/usr/include -I${STAGING_INCDIR}/graphite-client/gcs -I${STAGING_INCDIR}/graphite-client/osal""

do_install:append () {
  if [ -d "${WORKDIR}/mixers/${BASEMACHINE}" ] && [ $(ls -1 ${WORKDIR}/mixers/${BASEMACHINE} | wc -l) -ne 0 ]; then
    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/mixers/${BASEMACHINE}/* ${D}${sysconfdir}/
  fi
}

FILES:${PN}     += "${libdir}/sound_trigger.primary.default.so"
