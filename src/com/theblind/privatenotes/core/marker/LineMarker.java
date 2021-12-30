package com.theblind.privatenotes.core.marker;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.intellij.codeInsight.daemon.GutterName;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.MarkupEditorFilter;
import com.intellij.openapi.editor.markup.MarkupEditorFilterFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.Function;
import com.theblind.privatenotes.action.anaction.EditNoteAnActionByLine;
import com.theblind.privatenotes.core.Config;
import com.theblind.privatenotes.core.PrivateNotesFactory;
import com.theblind.privatenotes.core.service.ConfigService;
import com.theblind.privatenotes.core.service.NoteFileService;
import com.theblind.privatenotes.core.util.IdeaApiUtil;
import com.theblind.privatenotes.core.util.PrivateNotesUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class LineMarker implements LineMarkerProvider {
    NoteFileService noteFileService = PrivateNotesFactory.getNoteFileService();
    ConfigService configService = PrivateNotesFactory.getConfigService();

    TimedCache<String, Object> timedCache = CacheUtil.newTimedCache(6*1000);
    Map<String,Set> cache_line=new ConcurrentHashMap<>();

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement psiElement) {
        Config config = configService.get();
        int i = psiElement.getTextOffset();
        String ii = psiElement.getText();
        PsiFile containingFile = psiElement.getContainingFile();
        VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();
        Document document = PsiDocumentManager.getInstance(psiElement.getProject()).getDocument(containingFile);
        Set lineNumbers = cache_line.putIfAbsent(virtualFile.getPath(),new ConcurrentHashSet());

        try {
            int lineNumber = document.getLineNumber(i);
            if(lineNumbers.contains(lineNumber)){
                return null;
            }
            String note = noteFileService.getNote(virtualFile.getPath(), lineNumber, IdeaApiUtil.getBytes(virtualFile));

            //防止异常被 重复输出
            if (!timedCache.containsKey(virtualFile.getPath())) {
                timedCache.put(virtualFile.getPath(),null);
                PrivateNotesUtil.errLog(new RuntimeException(String.format("i:%s,i,virtualFile:%s",i,virtualFile.getPath())), psiElement.getProject());
                PrivateNotesUtil.errLog(new RuntimeException(String.format("i:%s,i,text:%s,lineNumber:%s",i,ii,lineNumber)), psiElement.getProject());
            }
            if(Objects.isNull(note)){
                lineNumbers.remove(lineNumber);
                return null;
            }
            lineNumbers.add(lineNumber);

            return new RunLineMarkerInfo(i,psiElement,IconLoader.findIcon("/icon/privateNotes.png"),(psi)->"私人注释");
        }catch (Exception e){
            //防止异常被 重复输出
            if (!timedCache.containsKey(virtualFile.getPath())) {
                timedCache.put(virtualFile.getPath(),null);
                PrivateNotesUtil.errLog(e, psiElement.getProject());
            }
        }
        return null;
    }





   /* @Override
    public void collectSlowLineMarkers(@NotNull List<? extends PsiElement> elements, @NotNull Collection<? super LineMarkerInfo<?>> result) {
        Set<Integer> lineNumbers = new HashSet<>();
        AtomicReference<Document> document=null;
        elements.stream().forEach(((psiElement)->{
            {
                Config config = configService.get();
                int i = psiElement.getTextOffset();
                String ii = psiElement.getText();
                PsiFile containingFile = psiElement.getContainingFile();
                VirtualFile virtualFile = containingFile.getVirtualFile();
                if (document.get() == null) {
                    document.set(PsiDocumentManager.getInstance(psiElement.getProject()).getDocument(containingFile));
                }
                try {
                    Integer lineNumber = document.get().getLineNumber(i);
                    if(lineNumbers.contains(lineNumber)){
                        return;
                    }

                    String note = noteFileService.getNote(virtualFile.getPath(), lineNumber, IdeaApiUtil.getBytes(virtualFile));

                    //防止异常被 重复输出
                    if (!timedCache.containsKey(virtualFile.getPath())) {
                        timedCache.put(virtualFile.getPath(),null);
                        PrivateNotesUtil.errLog(new RuntimeException(String.format("i:%s,i,virtualFile:%s",i,virtualFile.getPath())), psiElement.getProject());
                        PrivateNotesUtil.errLog(new RuntimeException(String.format("i:%s,i,text:%s,lineNumber:%s",i,ii,lineNumber)), psiElement.getProject());
                    }
                    if(Objects.isNull(note)){
                        lineNumbers.remove(lineNumber);
                        return;
                    }
                    lineNumbers.add(lineNumber);
                    result.add(new RunLineMarkerInfo(i,psiElement,IconLoader.findIcon("/icon/privateNotes.png"),(psi)->"私人注释")) ;
                }catch (Exception e){
                    //防止异常被 重复输出
                    if (!timedCache.containsKey(virtualFile.getPath())) {
                        timedCache.put(virtualFile.getPath(),null);
                        PrivateNotesUtil.errLog(e, psiElement.getProject());
                    }
                }
            }
        }));
    }*/

 /*

    static abstract class AbstractElementFilter {

        protected abstract Collection<? extends DomElement> getResults(@NotNull PsiElement element);

        public void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
            final Collection<? extends DomElement> results = getResults(element);
            if (!results.isEmpty()) {
                final List<XmlTag> xmlTags = results.stream().map(DomElement::getXmlTag).collect(Collectors.toList());
                NavigationGutterIconBuilder<PsiElement> builder =
                        NavigationGutterIconBuilder.create(IconLoader.findIcon("/icon/privateNotes.png"))
                                .setAlignment(GutterIconRenderer.Alignment.CENTER)
                                .setCellRenderer(new GotoMapperXmlSchemaTypeRendererProvider.MyRenderer())
                                .setTargets(xmlTags)
                                .setTooltipTitle("Navigation to target in mapper xml");
                final PsiElement targetMarkerInfo = Objects.requireNonNull(((PsiNameIdentifierOwner) element).getNameIdentifier());
                result.add(builder.createLineMarkerInfo(targetMarkerInfo));
            }
        }
*/

    static class RunLineMarkerInfo extends LineMarkerInfo<PsiElement> {

        int i;

        RunLineMarkerInfo(int i,PsiElement element, Icon icon, Function<PsiElement, String> tooltipProvider) {
            super(element, element.getTextRange(), icon,  tooltipProvider, null, GutterIconRenderer.Alignment.LEFT);
            this.i=i;
        }

        @Override
        public GutterIconRenderer createGutterRenderer() {
            return new LineMarkerGutterIconRenderer<PsiElement>(this) {
                @Override
                public AnAction getClickAction() {
                    return new EditNoteAnActionByLine(i);
                }

                @Override
                public boolean isNavigateAction() {
                    return true;
                }


            };
        }

        @NotNull
        public MarkupEditorFilter getEditorFilter() {
            MarkupEditorFilter var10000 = MarkupEditorFilterFactory.createIsNotDiffFilter();
            return var10000;
        }


    }
}
