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
OMX_ENCODERS_qcs610  = "True"

RDEPENDS_packagegroup-qti-audio = ' \
    ${@bb.utils.contains("COMBINED_FEATURES", "qti-audio", "audiodlkm init-audio audiohal encoders", "", d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "qti-audio qti-audio-encoder", "encoders", "", d)} \
    ${@bb.utils.contains("OMX_ENCODERS", "True", "encoders", "", d)} \
'
