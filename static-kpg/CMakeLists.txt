cmake_minimum_required(VERSION 3.4.1)

add_library( avcodec
             STATIC
             IMPORTED )
set_target_properties( # Specifies the target library.
                       avcodec

                       # Specifies the parameter you want to define.
                       PROPERTIES IMPORTED_LOCATION

                       # Provides the path to the library you want to import.
                       ${CMAKE_SOURCE_DIR}/src/main/libs/armeabi-v7a/libavcodec.a )

add_library(avutil
            STATIC
            IMPORTED )
set_target_properties( # Specifies the target library.
                      avutil

                      # Specifies the parameter you want to define.
                      PROPERTIES IMPORTED_LOCATION

                      # Provides the path to the library you want to import.
                      ${CMAKE_SOURCE_DIR}/src/main/libs/armeabi-v7a/libavutil.a )
add_library(swresample
          STATIC
          IMPORTED )
set_target_properties( # Specifies the target library.
                    swresample

                    # Specifies the parameter you want to define.
                    PROPERTIES IMPORTED_LOCATION

                    # Provides the path to the library you want to import.
                    ${CMAKE_SOURCE_DIR}/src/main/libs/armeabi-v7a/libswresample.a )
add_library(swscale
          STATIC
          IMPORTED )
set_target_properties( # Specifies the target library.
                    swscale

                    # Specifies the parameter you want to define.
                    PROPERTIES IMPORTED_LOCATION

                    # Provides the path to the library you want to import.
                    ${CMAKE_SOURCE_DIR}/src/main/libs/armeabi-v7a/libswscale.a )
find_library(z-lib z)
find_library(m-lib m)
find_library(log-lib log)
find_library(android-lib android)

add_library(kpg STATIC ${CMAKE_SOURCE_DIR}/src/main/jni/libkpg.cpp)
include_directories(${CMAKE_SOURCE_DIR}/src/main/jni/include/)
target_link_libraries(kpg ${z-lib} ${m-lib} avcodec swresample swscale avutil ${log-lib} ${android-lib})