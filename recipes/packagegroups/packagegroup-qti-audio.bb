SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

RDEPENDS_packagegroup-qti-audio = ' \
    ${@bb.utils.contains("COMBINED_FEATURES", "qti-audio", "audiodlkm init-audio audiohal", "", d)} \
    ${@bb.utils.contains_any("BASEMACHINE", ["sdxpoorwills","mdm9607","mdm9650"], "alsa-intf" , "", d)} \
    ${@bb.utils.contains("COMBINED_FEATURES", "qti-audio qti-audio-encoder", "encoders", "", d)} \
'
