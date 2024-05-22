include $(TOPDIR)/rules.mk

PKG_NAME:=qahw-api
PKG_RELEASE:=1
PKG_LICENSE:=BSD-3-Clause
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/audio-hal/primary-hal/qahw_api

include $(INCLUDE_DIR)/package.mk

##PKG_BUILD_DEPENDS:=audio_dlkm_kernel

define Package/qahw-api
  SECTION:=AUDIO
  CATEGORY:=AUDIO
  MAINTAINER:=QTI MM Audio
  TITLE:=QAHW-API
  DEPENDS:= +libhardware +libcutils +libion +glib2 +tinycompress +tinyalsa +libexpat +pal +acdbdata +media-headers +audio-utils +qahw +core-include
endef

define Package/qahw-api/description
	QAHW-API
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
	-I$(STAGING_DIR)/usr/include/system/audio_android_q \
	-I$(STAGING_DIR)/usr/include/qahw

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

define Build/InstallDev
	$(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include/qahw-api/
	mkdir -p $(1)/usr/lib/
	$(CP) $(PKG_BUILD_DIR)/inc/*.h $(1)/usr/include/qahw-api/
	$(CP) $(PKG_BUILD_DIR)/test/*.h $(1)/usr/include/qahw-api/
	$(CP) $(PKG_BUILD_DIR)/.libs/libqahw.so* $(1)/usr/lib/
endef

define Package/qahw-api/install
	$(INSTALL_DIR) $(1)/usr/bin/
	$(INSTALL_DIR) $(1)/usr/lib/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/hal_play_test $(1)/usr/bin/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/hal_rec_test $(1)/usr/bin/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/hal_voice_test $(1)/usr/bin/
	$(CP) $(PKG_BUILD_DIR)/.libs/libqahw.so* $(1)/usr/lib/
endef

$(eval $(call BuildPackage,qahw-api))
