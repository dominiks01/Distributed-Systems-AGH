cmake_minimum_required(VERSION 3.27)

project(SmartHomeClient LANGUAGES CXX)
set(CMAKE_CXX_EXTENSIONS OFF)
option(BUILD_SHARED_LIBS "Build shared libraries" OFF)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/bin)

# Add your source files
set(SOURCES
        main.cpp
        remote/OutdoorLightsHandler.hpp
        remote/CommandHandler.hpp
        remote/SmartDeviceHandler.hpp
        utils/utils.hpp
        enums/SmartInterfaceEnums.hpp
        enums/OutdoorLightInterfaceEnums.hpp
        remote/LightControlHandler.hpp
        enums/LightControlInterfaceEnums.hpp
        enums/DetectorInterfaceEnums.hpp
        remote/DetectorsHandler.hpp
        remote/MoveDetectorsHandler.hpp
        enums/MoveDetectorEnum.hpp
        enums/SmokeDetectorEnum.hpp
        remote/SmokeDetectorsHandler.hpp
)

# Find required packages
find_package(glog 0.7.0 REQUIRED)
find_package(Ice REQUIRED)

# Include directories
include_directories(${ICE_INCLUDE_DIRS})

# Add executable
add_executable(SmartHomeClient ${SOURCES})

# Link libraries
target_link_libraries(SmartHomeClient ${ICE_LIBRARIES} glog::glog)

# Install target
install(TARGETS SmartHomeClient)
