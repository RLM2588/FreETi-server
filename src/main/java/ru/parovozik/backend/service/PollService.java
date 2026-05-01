package ru.parovozik.backend.service;

import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.PollRequest;
import ru.parovozik.backend.entity.Poll;
import ru.parovozik.backend.entity.Vote;
import ru.parovozik.backend.model.Status;
import ru.parovozik.backend.repostitory.PollRepository;
import ru.parovozik.backend.repostitory.VoteRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PollService {
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;


    public PollService(PollRepository pollRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }

    public boolean createPoll(PollRequest pollRequest) {
        try {
            Poll poll = new Poll();
            poll.setTitle(pollRequest.title());
            poll.setStatus(pollRequest.status());
            poll.setVar1(poll.getVar1());
            poll.setVar2(poll.getVar2());
            poll.setGroupEvent(pollRequest.groupEvents());
            if (poll.getVar3() != null) poll.setVar3(poll.getVar3());
            if (poll.getVar4() != null) poll.setVar4(poll.getVar4());
            if (poll.getVar5() != null) poll.setVar5(poll.getVar5());
            pollRepository.save(poll);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<String> getOptions(UUID uuid) {
        try{
            Poll poll = pollRepository.findPollById(uuid);
            return List.of(poll.getVar1(),poll.getVar2(),poll.getVar3(), poll.getVar4(), poll.getVar5());
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<Vote> returnVotes(UUID uuid) {
        try{
            Poll poll = pollRepository.findPollById(uuid);
            return voteRepository.findAllByPoll(poll);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean changeStatus(UUID uuid, Status status) {
        try{
            Poll poll = pollRepository.findPollById(uuid);
            poll.setStatus(status);
            pollRepository.save(poll);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean deletePoll(UUID uuid) {
        try{
            Poll poll = pollRepository.findPollById(uuid);
            pollRepository.delete(poll);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
