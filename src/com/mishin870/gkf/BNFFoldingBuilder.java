package com.mishin870.gkf;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Comment block folding builder
 */
public class BNFFoldingBuilder extends FoldingBuilderEx {
	private static final String START_SEQUENCE = "//BLOCK";
	private static final String END_SEQUENCE = "//END";
	
	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
		FoldingGroup group = FoldingGroup.newGroup("bnf.blocks");
		
		final List<FoldingDescriptor> descriptors = new ArrayList<>();
		final List<PsiComment> comments = PsiUtil.getAllComments(root);
		
		PsiComment startBlock = null;
		String blockName = null;
		
		for (final PsiComment comment : comments) {
			final String text = comment.getText();
			
			if (text.startsWith(START_SEQUENCE)) {
				blockName = text.substring(START_SEQUENCE.length()).trim();
				startBlock = comment;
			} else if (startBlock != null && text.trim().equals(END_SEQUENCE)) {
				final String currentBlockName = blockName;
				
				descriptors.add(new FoldingDescriptor(root.getNode(),
						new TextRange(
								startBlock.getTextRange().getStartOffset(),
								comment.getTextRange().getEndOffset()
						), group) {
					@NotNull
					@Override
					public String getPlaceholderText() {
						return currentBlockName;
					}
				});
				
				startBlock = null;
				blockName = null;
			}
		}
		
		return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
	}
	
	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode node) {
		return "...";
	}
	
	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node) {
		return true;
	}
}