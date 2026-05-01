package ru.parovozik.backend.dto;

import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.Groups;
import ru.parovozik.backend.model.Status;

public record PollRequest(String title, Status status, GroupEvents groupEvents, String var1, String var2, String var3, String var4, String var5) {
}
