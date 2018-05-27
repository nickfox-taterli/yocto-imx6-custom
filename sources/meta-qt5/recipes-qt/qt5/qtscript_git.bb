require qt5.inc
require qt5-git.inc

LICENSE = "GFDL-1.3 & BSD & ( GPL-3.0 & The-Qt-Company-GPL-Exception-1.0 | The-Qt-Company-Commercial ) & ( GPL-2.0+ | LGPL-3.0 | The-Qt-Company-Commercial )"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv21;md5=4bfd28363f541b10d9f024181b8df516 \
    file://LICENSE.LGPLv3;md5=e0459b45c5c4840b353141a8bbed91f0 \
    file://LICENSE.GPLv3;md5=88e2b9117e6be406b5ed6ee4ca99a705 \
    file://LGPL_EXCEPTION.txt;md5=9625233da42f9e0ce9d63651a9d97654 \
    file://LICENSE.FDL;md5=6d9f2a9af4c8b8c3c769f6cc1b6aaf7e \
"

# qemuarm build fails with:
# /OE/build/oe-core/tmp-glibc/work/armv5te-oe-linux-gnueabi/qtscript/5.4.1+gitAUTOINC+822df36f25-r0/git/src/3rdparty/javascriptcore/JavaScriptCore/assembler/AssemblerBuffer.h: In member function 'void QTJSC::AssemblerBuffer::putInt64Unchecked(int64_t)':
#/OE/build/oe-core/tmp-glibc/work/armv5te-oe-linux-gnueabi/qtscript/5.4.1+gitAUTOINC+822df36f25-r0/git/src/3rdparty/javascriptcore/JavaScriptCore/assembler/AssemblerBuffer.h:106:58: warning: cast from 'char*' to 'int64_t* {aka long long int*}' increases required alignment of target type [-Wcast-align]
#             *reinterpret_cast<int64_t*>(&m_buffer[m_size]) = value;
#                                                          ^
#{standard input}: Assembler messages:
#{standard input}:22: Error: selected processor does not support Thumb mode `stmdb sp!,{r1-r3}'
#{standard input}:23: Error: selected processor does not support Thumb mode `stmdb sp!,{r4-r8,lr}'
#{standard input}:30: Error: lo register required -- `ldmia sp!,{r4-r8,lr}'
#{standard input}:43: Error: lo register required -- `ldmia sp!,{r4-r8,lr}'
ARM_INSTRUCTION_SET_armv4 = "arm"
ARM_INSTRUCTION_SET_armv5 = "arm"

DEPENDS += "qtbase"

SRCREV = "c35df2ec0ee05351912e768d298a7c627dbdd1ef"
