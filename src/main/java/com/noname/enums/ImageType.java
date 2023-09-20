package com.noname.enums;

public enum ImageType {
        USER("C:\\upload\\user\\"),
        PRODUCT("C:\\upload\\product\\");

        private final String uploadPath;

        private ImageType(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public String getUploadPath() {
            return uploadPath;
        }


}
