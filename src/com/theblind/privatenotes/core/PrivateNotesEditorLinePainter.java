package com.theblind.privatenotes.core;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.crypto.digest.MD5;
import com.intellij.openapi.editor.EditorLinePainter;
import com.intellij.openapi.editor.LineExtensionInfo;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.theblind.privatenotes.core.service.NoteFileService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PrivateNotesEditorLinePainter  extends EditorLinePainter {

    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();

    @Nullable
    @Override
    public Collection<LineExtensionInfo> getLineExtensions(@NotNull Project project, @NotNull VirtualFile virtualFile, int i) {
        /*  Color hsbColor = JBColor.getHSBColor(0.0030674834f, 0.652f, 0.98039216f);*/
        List<LineExtensionInfo> result = new ArrayList<>();//✍
        try {
            String note = noteFileService.getNote(virtualFile.getPath(), i);
            if(Objects.isNull(note)){
                return null;
            }
            result.add(new LineExtensionInfo("  ◄ " ,
                    new TextAttributes(null, null, JBColor.blue, null, Font.PLAIN)));
            result.add(new LineExtensionInfo(note,
                    new TextAttributes(null, null, JBColor.gray, null, Font.PLAIN)));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    public static void main(String[] args) {
        //250,90,87
        //
        //0.0030674834-0.652-0.98039216
        //1.1042944-0.652-0.98039216
        float[] floats = JBColor.RGBtoHSB(250, 90, 87, null);
        float[] floats1 = rgb2hsb(250, 90, 87);
        System.out.println(floats[0]+"-"+floats[1]+"-"+floats[2]);
        System.out.println(floats1[0]+"-"+floats1[1]+"-"+floats1[2]);
    }

    public static float[] rgb2hsb(int rgbR, int rgbG, int rgbB) {
        assert 0 <= rgbR && rgbR <= 255;
        assert 0 <= rgbG && rgbG <= 255;
        assert 0 <= rgbB && rgbB <= 255;
        int[] rgb = new int[] { rgbR, rgbG, rgbB };
        Arrays.sort(rgb);
        int max = rgb[2];
        int min = rgb[0];

        float hsbB = max / 255.0f;
        float hsbS = max == 0 ? 0 : (max - min) / (float) max;

        float hsbH = 0;
        if (max == rgbR && rgbG >= rgbB) {
            hsbH = (rgbG - rgbB) * 60f / (max - min) + 0;
        } else if (max == rgbR && rgbG < rgbB) {
            hsbH = (rgbG - rgbB) * 60f / (max - min) + 360;
        } else if (max == rgbG) {
            hsbH = (rgbB - rgbR) * 60f / (max - min) + 120;
        } else if (max == rgbB) {
            hsbH = (rgbR - rgbG) * 60f / (max - min) + 240;
        }

        return new float[] { hsbH, hsbS, hsbB };
    }
}
