include $(TOPDIR)/rules.mk

PKG_NAME:=hal-pal
PKG_RELEASE:=1
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/audio-hal/primary-hal/hal-pal

include $(INCLUDE_DIR)/package.mk

##PKG_BUILD_DEPENDS:=audio_dlkm_kernel

define Package/hal-pal
  SECTION:=AUDIO
  CATEGORY:=AUDIO
  MAINTAINER:=QTI MM Audio
  TITLE:=HAL-PAL
  DEPENDS:= +libhardware +libcutils +libion +glib2 +tinycompress +tinyalsa +libexpat +pal +acdbdata +media-headers +audio-utils +qahw +core-include
endef

define Package/hal-pal/description
	HAL-PAL
endef

CONFIGURE_ARGS += \
	--with-openwrt \
	--with-glib

TARGET_CFLAGS += \
	-DFEATURE_IPQ_OPENWRT \
	-I$(STAGING_DIR)/usr/include \
	-I$(STAGING_DIR)/usr/include/audio_effects/ \
	-I$(STAGING_DIR)/usr/include/glib-2.0 \
	-I$(TOPDIR)/src/vendor/qcom/opensource/audio-hal/primary-hal/hal/audio_extn \
	-I$(STAGING_DIR)/usr/include/pal \
	-I$(STAGING_DIR)/usr/include/pal/inc \
	-I$(STAGING_DIR)/usr/include/system \
	-I$(STAGING_DIR)/usr/include/system/audio_android_q

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

define Build/InstallDev
	$(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include/hal-pal/
	mkdir -p $(1)/usr/lib/
	mkdir -p $(1)/etc
	$(CP) $(PKG_BUILD_DIR)/*.h $(1)/usr/include/hal-pal/
	$(CP) $(PKG_BUILD_DIR)/audio_extn/*.h $(1)/usr/include/hal-pal/
	$(CP) $(PKG_BUILD_DIR)/.libs/audio.primary.default.so* $(1)/usr/lib/
	$(CP) ./files/sdxpinn/* $(1)/etc
endef

define Package/hal-pal/install
	$(INSTALL_DIR) $(1)/usr/lib/
	$(INSTALL_DIR) $(1)/etc
	$(INSTALL_DIR) $(1)/data/audio
	$(CP) $(PKG_BUILD_DIR)/.libs/audio.primary.default.so* $(1)/usr/lib/
	$(CP) ./files/sdxpinn/* $(1)/etc
endef

$(eval $(call BuildPackage,hal-pal))
