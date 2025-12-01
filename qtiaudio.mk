include $(INCLUDE_DIR)/target.mk
QTIAUDIO:=init-audio kmod-audio-kernel tinyalsa tinycompress qahw hal-pal qahw-api
ifeq ($(PRPL_VERSION),3.1)
	QTIAUDIO:=
endif