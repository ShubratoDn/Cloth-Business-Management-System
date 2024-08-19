package com.cloth.business.payloads;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageResponse {
	
	private List<?> content = new ArrayList<>();
	
	private int page;
	
	private int size;
	
	private long totalElements;
	
	private int totalPages;
	
	private int numberOfElements;
	
	private boolean isEmpty;
	
	private boolean isFirst;
	
	private boolean isLast;
	
	
}
