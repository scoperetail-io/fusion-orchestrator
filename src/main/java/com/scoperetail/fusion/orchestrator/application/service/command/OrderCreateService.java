package com.scoperetail.fusion.orchestrator.application.service.command;

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

import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaJMS;
import static com.scoperetail.fusion.orchestrator.common.Event.OrderCreateViaREST;
import com.scoperetail.fusion.core.application.port.in.command.create.PosterUseCase;
import com.scoperetail.fusion.core.application.port.out.jms.PosterOutboundJmsPort;
import com.scoperetail.fusion.orchestrator.application.port.in.command.create.OrderCreateUseCase;
import com.scoperetail.fusion.orchestrator.domain.OrderCreateRequest;
import com.scoperetail.fusion.shared.kernel.common.annotation.UseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class OrderCreateService implements OrderCreateUseCase {

  private PosterUseCase posterUseCase;
  private PosterOutboundJmsPort posterOutboundJmsPort;

  @Override
  public void handleRestEvent(final OrderCreateRequest orderCreateRequest) throws Exception {
    posterUseCase.post(OrderCreateViaREST.name(), orderCreateRequest, true);
  }

  @Override
  public void createOrder(final Object event, final boolean isValid) throws Exception {
    posterUseCase.post(OrderCreateViaJMS.name(), event, isValid);
  }

  @Override
  public void send(final String brokerId, final String queueName, final String message) {
    posterOutboundJmsPort.post(brokerId, queueName, message);
  }
}
