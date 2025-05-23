package com.example.inovaTest.repositories;

import com.example.inovaTest.enums.FriendshipStatus;
import com.example.inovaTest.models.FriendshipModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipModel, UUID> {

    Optional<FriendshipModel> findBySenderAndReceiver(UserModel sender, UserModel receiver);

    // Buscar todas as amizades onde o usuário participa (como sender ou receiver)
    @Query("SELECT f FROM FriendshipModel f WHERE f.sender = :user OR f.receiver = :user")
    List<FriendshipModel> findBySenderOrReceiver(@Param("user") UserModel user1, @Param("user") UserModel user2);

    // Buscar amizades com status específico onde o usuário participa
    @Query("SELECT f FROM FriendshipModel f WHERE (f.sender = :user1 OR f.receiver = :user1) AND f.status = :status")
    List<FriendshipModel> findBySenderOrReceiverAndStatus(
        @Param("user1") UserModel user1, 
        @Param("user1") UserModel user2, 
        @Param("status") FriendshipStatus status
    );

    // Busca amizades pendentes recebidas por um usuário
    @Query("SELECT f FROM FriendshipModel f WHERE f.receiver = :user AND f.status = 'PENDING'")
    List<FriendshipModel> findPendingRequestsReceived(@Param("user") UserModel user);

    // Busca amizades pendentes enviadas por um usuário
    @Query("SELECT f FROM FriendshipModel f WHERE f.sender = :user AND f.status = 'PENDING'")
    List<FriendshipModel> findPendingRequestsSent(@Param("user") UserModel user);

    // Busca amizades aceitas de um usuário
    @Query("SELECT f FROM FriendshipModel f WHERE (f.sender = :user OR f.receiver = :user) AND f.status = 'ACCEPTED'")
    List<FriendshipModel> findAcceptedFriendships(@Param("user") UserModel user);

    // Verifica se existe amizade entre dois usuários (em qualquer direção)
    @Query("SELECT f FROM FriendshipModel f WHERE " +
           "(f.sender = :user1 AND f.receiver = :user2) OR " +
           "(f.sender = :user2 AND f.receiver = :user1)")
    Optional<FriendshipModel> findFriendshipBetweenUsers(
        @Param("user1") UserModel user1, 
        @Param("user2") UserModel user2
    );

    // Contar amigos de um usuário
    @Query("SELECT COUNT(f) FROM FriendshipModel f WHERE (f.sender = :user OR f.receiver = :user) AND f.status = 'ACCEPTED'")
    Long countFriendsByUser(@Param("user") UserModel user);
}