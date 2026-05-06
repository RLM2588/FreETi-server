package ru.parovozik.backend.service;

import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.PollRequest;
import ru.parovozik.backend.entity.Poll;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.entity.Vote;
import ru.parovozik.backend.repostitory.PollRepository;
import ru.parovozik.backend.repostitory.UserRepository;
import ru.parovozik.backend.repostitory.VoteRepository;

import java.util.UUID;

@Service
public class VotingService {
    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

    public VotingService(UserRepository userRepository, PollRepository pollRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }


    public boolean createVote(UUID pollUUID, String username, int choose) {
            try {
                Poll poll = pollRepository.findPollById(pollUUID);
                User user = userRepository.findUserByUsername(username);
                Vote vote = new Vote();
                vote.setPoll(poll);
                vote.setUserID(user);
                vote.setVariant(choose);
                voteRepository.save(vote);
                return true;
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
    }

    public boolean deleteVote(UUID pollUUID, String username, int choose) {
        try {
            Poll poll = pollRepository.findPollById(pollUUID);
            User user = userRepository.findUserByUsername(username);
            Vote vote = voteRepository.findByUserIDAndPoll(user,poll);
            voteRepository.delete(vote);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}
