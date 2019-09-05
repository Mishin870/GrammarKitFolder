package com.mishin870.gkf;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.intellij.grammar.BnfFileType;
import org.intellij.grammar.psi.BnfFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsiUtil {
	
	/**
	 * Recursively collects all comment PSI nodes
	 *
	 * @param element root element of the current recursion step
	 * @param comments list of collected comment nodes
	 */
	private static void collectComments(PsiElement element, List<PsiComment> comments) {
		for (PsiElement child : element.getChildren()) {
			if (child instanceof PsiComment) {
				comments.add((PsiComment) child);
			} else {
				collectComments(child, comments);
			}
		}
	}
	
	/**
	 * @param element root element
	 * @return list of comments within root element
	 */
	public static List<PsiComment> getAllComments(PsiElement element) {
		List<PsiComment> result = new ArrayList<>();
		collectComments(element, result);
		return result;
	}
	
	/**
	 * @param project project to search comments
	 * @return list of lists of comments for each virtual file within project
	 */
	public static List<List<PsiComment>> getAllComments(Project project) {
		List<List<PsiComment>> result = new ArrayList<>();
		Collection<VirtualFile> virtualFiles =
				FileTypeIndex.getFiles(BnfFileType.INSTANCE, GlobalSearchScope.allScope(project));
		
		for (VirtualFile virtualFile : virtualFiles) {
			List<PsiComment> fileResult = new ArrayList<>();
			
			BnfFile bnfFile = (BnfFile) PsiManager.getInstance(project).findFile(virtualFile);
			
			if (bnfFile != null) {
				collectComments(bnfFile, fileResult);
			}
			
			result.add(fileResult);
		}
		return result;
	}
	
}
