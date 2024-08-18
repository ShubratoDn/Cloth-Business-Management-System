package com.cloth.business.helpers;

import org.springframework.data.domain.Page;

import com.cloth.business.payloads.PageResponse;

public class HelperUtils {

	public static PageResponse pageToPageResponse(Page<?> pageInfo) {
		PageResponse pageData = new PageResponse();
		pageData.setContent(pageInfo.getContent());
		pageData.setPage(pageInfo.getNumber());
		pageData.setSize(pageInfo.getSize());
		pageData.setTotalElements(pageInfo.getTotalElements());
		pageData.setTotalPages(pageInfo.getTotalPages());
		pageData.setNumberOfElements(pageInfo.getNumberOfElements());

		pageData.setEmpty(pageInfo.isEmpty());
		pageData.setFirst(pageInfo.isFirst());
		pageData.setLast(pageInfo.isLast());
		return pageData;
	}
}
