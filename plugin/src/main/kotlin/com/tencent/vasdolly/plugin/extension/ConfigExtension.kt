/*
 * Tencent is pleased to support the open source community by making VasDolly available.
 *
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tencent.vasdolly.plugin.extension

import com.tencent.vasdolly.plugin.model.Channel
import org.gradle.api.Project
import java.io.File

// base config extension class for plugin extension
open class ConfigExtension(var project: Project) {
    //only fit v2 signature
    //低内存模式（仅针对V2签名，默认为false）：
    //只把签名块、中央目录和EOCD读取到内存，不把最大头的内容块读取到内存
    //在手机上合成APK时，可以使用该模式
    var lowMemory = false

    //是否为快速模式，即不验证渠道名
    var fastMode = false

    //渠道列表文件
    var channelFile: File? = null

    //渠道分割符号
    var channelSeparator: String? = null

    //渠道包保持目录
    var outputDir: File = File(project.buildDir,"channel")

    /**
     * 从扩展属性中获取channelFile配置的扩展渠道列表
     */
    fun getExtensionChannelList(): List<Channel> {
        val channelList = mutableListOf<Channel>()
        if (channelFile != null && channelFile?.isFile!! && channelFile?.exists()!!) {
            channelFile?.forEachLine { channel ->
                if (channel.isNotEmpty()) {
                    if (channelSeparator.isNullOrEmpty()) {
                        val channelModel = Channel(channel, null)
                        channelList.add(channelModel)
                    } else {
                        val split = channel.split(channelSeparator!!)
                        val channelModel =
                            Channel(split.first(), if (split.size > 1) split[1] else null)
                        channelList.add(channelModel)
                    }
                }
            }
            println("get channels from `channelFile`,channels:$channelList")
        }
        return channelList
    }
}

