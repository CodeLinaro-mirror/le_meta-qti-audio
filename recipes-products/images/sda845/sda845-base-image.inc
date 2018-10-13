# Rootfs creation.
#64 bit libs
IMAGE_INSTALL += "audiohal"
IMAGE_INSTALL += "tinyalsa"
IMAGE_INSTALL += "tinycompress"
IMAGE_INSTALL += "init-audio"
IMAGE_INSTALL += "qahw"
IMAGE_INSTALL += "encoders"
IMAGE_INSTALL += "audioalsa"
IMAGE_INSTALL += "alsa-utils"

# Following packages support only 32-bit compilation.
# When 32-bit multilib is enabled, compile them with lib32- prefix.

IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-audiohal', 'audiohal', d)}"
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-tinyalsa', 'tinyalsa', d)}"
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-tinycompress', 'tinycompress', d)}"
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-init-audio', 'init-audio', d)}"
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-qahw', 'qahw', d)}"
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-encoders', 'encoders', d)}"
IMAGE_INSTALL += "${@base_contains('MULTILIB_VARIANTS', 'lib32', 'lib32-audioalsa', 'audioalsa', d)}"
