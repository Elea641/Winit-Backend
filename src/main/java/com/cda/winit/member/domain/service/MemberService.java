package com.cda.winit.member.domain.service;

import com.cda.winit.member.domain.dto.MemberDto;
import com.cda.winit.member.domain.dto.MemberRequest;
import com.cda.winit.member.domain.service.interfaces.IMemberService;
import com.cda.winit.member.infrastructure.exception.MemberServiceException;
import com.cda.winit.sport.domain.service.SportService;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.team.insfrastructure.repository.TeamRepository;
import com.cda.winit.tournament.domain.service.TournamentService;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.UserService;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService implements IMemberService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TournamentService tournamentService;

    public MemberDto createMember(String teamName, MemberRequest memberRequest) {
        Team team = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new MemberServiceException("Aucune équipe trouvée avec ce nom: " + teamName));

        User user = userRepository.findByEmail(memberRequest.getEmail())
                .orElseThrow(() -> new MemberServiceException("Aucun utilisateur trouvé avec cet identifiant: " + memberRequest.getEmail()));

        boolean isMember = team.getUsers().contains(user);

        if (isMember) {
            throw new MemberServiceException("L'utilisateur est déjà inscrit dans cette équipe");
        } else {
            int maxParticipantsBySport = team.getSport().getNumberOfPlayers();
            int currentParticipants = teamRepository.countUsersByTeamId(team.getId());

            if (currentParticipants >= maxParticipantsBySport) {
                throw new MemberServiceException("La limite de participants dans cette équipe est atteinte");
            }

            team.getUsers().add(user);
            teamRepository.save(team);

            MemberDto memberDto = new MemberDto();
            memberDto.setId(user.getId());
            memberDto.setFirstName(user.getFirstName());
            memberDto.setLastName(user.getLastName());

            return memberDto;
        }
    }

    public void deleteMember(String teamName, Long memberId) throws Exception {
        Team team = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new MemberServiceException("Aucune équipe trouvée avec ce nom: " + teamName));

        User currentUser = userService.getCurrentUser().orElseThrow(() -> new MemberServiceException("Utilisateur actuel non trouvé."));

        User teamLeader = team.getUser();

        boolean isTeamInTournament = tournamentService.verifyTeamInTournament(team.getId());

        if (isTeamInTournament) {
            throw new MemberServiceException("Cette équipe est associée à un tournoi");
        }

        if (!Objects.equals(currentUser.getId(), teamLeader.getId())) {
            throw new MemberServiceException("Vous n'êtes pas le propriétaire de l'équipe " + teamName);
        }

        Optional<User> optionalMember = userRepository.findById(memberId);
        if (!optionalMember.isPresent()) {
            throw new MemberServiceException("L'utilisateur avec l'ID " + memberId + " n'existe pas.");
        }

        User member = optionalMember.get();
        if (!team.getUsers().contains(member)) {
            throw new MemberServiceException("L'utilisateur: " + member.getId() + " n'est pas dans l'équipe " + teamName);
        }

        team.getUsers().remove(member);
        teamRepository.save(team);
    }
}