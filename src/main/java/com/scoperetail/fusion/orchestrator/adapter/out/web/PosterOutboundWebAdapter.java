/* ScopeRetail (C)2021 */
package com.scoperetail.fusion.orchestrator.adapter.out.web;

import java.util.Map;

import com.scoperetail.fusion.orchestrator.application.port.out.web.PosterOutboundWebPort;
import com.scoperetail.fusion.shared.kernel.common.annotation.WebAdapter;

import lombok.AllArgsConstructor;

@WebAdapter
@AllArgsConstructor
public class PosterOutboundWebAdapter implements PosterOutboundWebPort {

	PosterOutboundHttpAdapter posterOutboundHttpAdapter;

	@Override
	public void post(final String url, final String methodType, final String requestBody,
			final Map<String, String> httpHeaders) {
		posterOutboundHttpAdapter.post(url, methodType, requestBody, httpHeaders);
	}
}
