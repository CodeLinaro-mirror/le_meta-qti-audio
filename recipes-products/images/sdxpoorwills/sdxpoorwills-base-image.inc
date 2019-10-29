# Rootfs creation.

IMAGE_INSTALL += "audiohal"
IMAGE_INSTALL += "tinyalsa"
IMAGE_INSTALL += "tinycompress"
IMAGE_INSTALL += "alsa-intf"
IMAGE_INSTALL += "audiodlkm"
IMAGE_INSTALL_append_auto += "binder"
