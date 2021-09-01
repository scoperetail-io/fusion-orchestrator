package com.scoperetail.fusion.orchestrator.adapter.in.web.command;

/*-
 * *****
 * fusion-orchestrator
 * -----
 * Copyright (C) 2018 - 2021 Scope Retail Systems Inc.
 * -----
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====
 */

import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaREST;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scoperetail.fusion.config.FusionConfig;
import com.scoperetail.fusion.core.adapter.in.web.command.AbstractBaseDelegate;
import com.scoperetail.fusion.core.adapter.in.web.command.HttpRequestHelper;
import com.scoperetail.fusion.core.application.port.in.command.AuditUseCase;
import com.scoperetail.fusion.core.application.port.in.command.DuplicateCheckUseCase;
import com.scoperetail.fusion.orchestrator.adapter.in.web.OrdersApiDelegate;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;
import com.scoperetail.fusion.orchestrator.domain.ModelApiResponse;
import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;
import com.scoperetail.fusion.shared.kernel.common.annotation.WebAdapter;

@WebAdapter
public class OrderCreateCommandDelegate extends AbstractBaseDelegate implements OrdersApiDelegate {

  private final OrderCreateUseCase orderCreateUseCase;

  public OrderCreateCommandDelegate(
      final DuplicateCheckUseCase duplicateCheckUseCase,
      final OrderCreateUseCase orderCreateUseCase,
      final AuditUseCase auditUseCase,
      final FusionConfig fusionConfig,
      final HttpRequestHelper httpRequestHelper) {
    super(duplicateCheckUseCase, auditUseCase, fusionConfig, httpRequestHelper);
    this.orderCreateUseCase = orderCreateUseCase;
  }

  @Override
  public ResponseEntity<ModelApiResponse> orderCreate(final OrderCreateRequest orderCreateRequest) {
    final HttpStatus result = doEvent(OrderCreateViaREST.name(), orderCreateRequest);
    return buildResponseEntity(result);
  }

  @Override
  protected HttpStatus processEvent(final Object domainEntity) throws Exception {
    orderCreateUseCase.handleRestEvent((OrderCreateRequest) domainEntity);
    return HttpStatus.ACCEPTED;
  }

  private ResponseEntity<ModelApiResponse> buildResponseEntity(final HttpStatus httpStatus) {
    final ModelApiResponse response = new ModelApiResponse();
    response.setCode(httpStatus.value());
    response.setMessage(httpStatus.getReasonPhrase());
    response.setType(httpStatus.name());
    return ResponseEntity.status(httpStatus).body(response);
  }
}
