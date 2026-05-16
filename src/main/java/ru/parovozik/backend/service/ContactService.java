package ru.parovozik.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.ContactAnswer;
import ru.parovozik.backend.entity.Contact;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.repostitory.ContactRepository;
import ru.parovozik.backend.repostitory.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public boolean addContact(ContactAnswer request) {
        try {
            Contact contact = new Contact(userRepository.findUserByUserId(request.user1()),userRepository.findUserByUserId(request.user2()),request.isFriend());
            return true;
        }
        catch (Exception e) {
            throw new RuntimeException("The programm had problem with adding the contact");
        }
    }


    @Transactional
    public boolean deleteContact(ContactAnswer request) {
        try{
            Contact contact = contactRepository.findAllByFirstAndSecond(userRepository.findUserByUserId(request.user1()),
                    userRepository.findUserByUserId(request.user2()));
            contactRepository.delete(contact);
            return true;
        }
        catch (Exception e) {
            throw new RuntimeException("The programm had problem with deleting the contact");
        }
    }

    @Transactional
    public ContactAnswer updateContact(ContactAnswer request) {
        try {
            if(contactRepository.findAllByFirstAndSecond(
                    userRepository.findUserByUserId(request.user1()),
                    userRepository.findUserByUserId(request.user2())
            ) != null) {
                this.addContact(request);
                return request;
            }

            contactRepository.updateIsFriend(userRepository.findUserByUserId(request.user1()),
                    userRepository.findUserByUserId(request.user2()), !request.isFriend());
            return new ContactAnswer(request.user1(), request.user2(), !request.isFriend());
        } catch (Exception e) {
            throw new RuntimeException("The programm had problem with updating the contact");
        }
    }

    public List<ContactAnswer> getContactAndFriends(String username) {
        List<Contact> contact1 = contactRepository.findAllByFirstAndIsFriend(
                userRepository.findUserByUsername(username),
                false
        );
        List<Contact> friends = contactRepository.findAllByFirstAndIsFriend(
                userRepository.findUserByUsername(username),
                true
        );
        return Stream.concat(contact1.stream(), friends.stream()).map(con -> new ContactAnswer(
                con.getFirst().getUserId(),
                con.getSecond().getUserId(),
                con.isFriend()
                )).collect(Collectors.toList());
    }
}
