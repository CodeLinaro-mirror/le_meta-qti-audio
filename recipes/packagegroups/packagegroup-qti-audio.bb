SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

# Support encoders only on selected SOCs.
OMX_ENCODERS  = "False"
OMX_ENCODERS:qcs610  = "True"
OMX_ENCODERS:qrb5165  = "True"

RDEPENDS:packagegroup-qti-audio = ' \
    ${@bb.utils.contains("COMBINED_FEATURES", "qti-audio", "audiodlkm init-audio audiohal", "", d)} \
    ${@bb.utils.contains("OMX_ENCODERS", "True", "encoders", "", d)} \
'
RDEPENDS:packagegroup-qti-audio:mdm9607 = ' \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-audio", "audiodlkm tinyalsa alsa-intf", "", d)} \
    ${@bb.utils.contains("OMX_ENCODERS", "True", "encoders", "", d)} \
'
RDEPENDS:packagegroup-qti-audio:remove:mdm9607 = "audcal audiodevicetree audio-datamgr audio-ftm soundtrigger qsthw qsthw-api "
RDEPENDS:packagegroup-qti-audio:remove:mdm9607 = "pa-qti-stt pa-qti-soundtrigger audio-generic-effect-framework audio-effects-noship pa-qti-effect"
