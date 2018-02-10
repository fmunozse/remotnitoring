package com.remotnitoring.web.rest;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.remotnitoring.domain.RequestRemoteCommand;
import com.remotnitoring.domain.ResponseRemoteCommand;
import com.remotnitoring.domain.enumeration.StatusRemoteCommand;
import com.remotnitoring.repository.RequestRemoteCommandRepository;
import com.remotnitoring.repository.ResponseRemoteCommandRepository;
import com.remotnitoring.web.rest.errors.InternalServerErrorException;
import com.remotnitoring.web.rest.util.PaginationUtil;
import com.remotnitoring.web.rest.vm.RemoteCommandVM;

@RestController
@RequestMapping("/api")
public class RemoteCommandResource {

	private final Logger log = LoggerFactory.getLogger(RemoteCommandResource.class);

	private final RequestRemoteCommandRepository requestRemoteCommandRepository;

	private final ResponseRemoteCommandRepository responseRemoteCommandRepository;

	public RemoteCommandResource(RequestRemoteCommandRepository requestRemoteCommandRepository,
			ResponseRemoteCommandRepository responseRemoteCommandRepository) {
		this.requestRemoteCommandRepository = requestRemoteCommandRepository;
		this.responseRemoteCommandRepository = responseRemoteCommandRepository;
	}

	

	@GetMapping(value = "/remote-command/{id}")
	@Timed
	@Transactional
	public ResponseEntity<List<ResponseRemoteCommand>> getResponseRemoteCommandsByRequest(
			@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get a page of ResponseRemoteCommands by request id {} ", id);
		Page<ResponseRemoteCommand> page = responseRemoteCommandRepository.findByIdRequestAndIsCurrentUser(id, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/remote-command/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/remote-command")
	@Timed
	public ResponseEntity<List<RemoteCommandVM>> getPendingCommands() {
		log.debug("RemoteCommand - getPendingCommands");

		List<RequestRemoteCommand> lRequestRemoteCommand = requestRemoteCommandRepository.findByUserIsCurrentUser();
		log.debug("Pending Commands: {} ", lRequestRemoteCommand);

		List<RemoteCommandVM> lRemoteCommandVM = new LinkedList<>();
		for (RequestRemoteCommand request : lRequestRemoteCommand) {

			RemoteCommandVM remoteCommandVM = new RemoteCommandVM();
			remoteCommandVM.setIdRequestRemoteCommand(request.getId());
			remoteCommandVM.setCommand(request.getCommand());
			remoteCommandVM.setDescription(request.getDescription());

			lRemoteCommandVM.add(remoteCommandVM);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Total-Count", Long.toString(lRemoteCommandVM.size()));
		return new ResponseEntity<>(lRemoteCommandVM, headers, HttpStatus.OK);
	}

	@PostMapping(value = "/remote-command")
	@Timed
	@Transactional
	public ResponseEntity<Void> postCommandExecuted(@Valid @RequestBody RemoteCommandVM remoteCommandVM) {
		log.debug("RemoteCommand - postCommandExecuted: {} ", remoteCommandVM);

		RequestRemoteCommand request = requestRemoteCommandRepository
				.findByUserAndIdAndPending(remoteCommandVM.getIdRequestRemoteCommand())
				.orElseThrow(() -> new InternalServerErrorException("RequestRemoteCommand not found or not correct "
						+ remoteCommandVM.getIdRequestRemoteCommand()));

		request.setStatus(StatusRemoteCommand.Completed);
		requestRemoteCommandRepository.save(request);

		ResponseRemoteCommand response = new ResponseRemoteCommand();
		response.setCodReturn(remoteCommandVM.getCodReturn());
		response.setLogResult(remoteCommandVM.getLogResult());
		response.setWhenExecuted(ZonedDateTime.now());
		response.setRequestRemoteCommand(request);
		responseRemoteCommandRepository.save(response);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
