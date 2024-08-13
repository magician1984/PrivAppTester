package com.android.privapptester.controller.module

import com.android.privapptester.core.IController.IFileController
import java.io.File

class FileController : IFileController {
    override fun listAllFiles(path: String, recursive: Boolean): List<IFileController.FileInfo> {
        val root = File(path)

        val files = mutableListOf<IFileController.FileInfo>()

        if (root.isFile) {
            files.add(IFileController.FileInfo(root.name, root.canRead()))
        } else if (root.isDirectory) {
            val maxDepth = if (recursive) Int.MAX_VALUE else 1
            root.walk()
                .maxDepth(maxDepth)
                .filter { it.isFile }
                .forEach { files.add(IFileController.FileInfo(it.name, it.canRead())) }
        }

        return files
    }

    override fun close() {}
}