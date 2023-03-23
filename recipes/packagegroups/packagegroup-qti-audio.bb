SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

PULSEAUDIO_PKGS = " \
    pulseaudio-server \
    pulseaudio-module-loopback \
    pulseaudio-module-null-source \
    pulseaudio-module-combine-sink \
    pulseaudio-module-switch-on-port-available \
    pulseaudio-misc \
    pulseaudio-module-role-cork \
    pulseaudio-module-role-exclusive \
    pulseaudio-module-role-ignore \
"

RDEPENDS:packagegroup-qti-audio += ' \
    ar-audiodlkm \
    init-audio \
    tinyalsa \
    tinycompress \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', '${PULSEAUDIO_PKGS}', '', d)} \
'
