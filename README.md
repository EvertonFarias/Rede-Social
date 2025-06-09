# Modelo Entidade-Relacionamento (MER)

## Entidades e Atributos

### UserModel
- **Atributos**:
  - id: UUID (PK)
  - login: String (unique, not null)
  - password: String (not null)
  - email: String (unique, not null)
  - verifiedEmail: boolean
  - enabled: boolean
  - role: UserRole (enum)
  - gender: GenderRole (enum)
  - dateOfBirth: LocalDate (not null)
  - profilePicture: String
  - profileDescription: String
- **Relacionamentos**:
  - 1:N **sentFriendRequests** (FriendshipModel.sender)
  - 1:N **receivedFriendRequests** (FriendshipModel.receiver)
  - 1:N **posts** (PostModel.user)
  - 1:N **comments** (CommentModel.user)
  - 1:N **likes** (LikeModel.user)
  - 1:N **follows** (FollowModel.follower)
  - 1:N **followers** (FollowModel.followed)
  - 1:N **notificationsSent** (NotificationModel.sender)
  - 1:N **notificationsReceived** (NotificationModel.recipient)
  - 1:1 **emailVerificationToken** (EmailVerificationToken.user)
  - 1:1 **passwordResetToken** (PasswordResetToken.user)

### PostModel
- **Atributos**:
  - id: UUID (PK)
  - user: UserModel (FK, not null)
  - content: String (not null)
  - imageUrl: String
  - videoUrl: String
  - createdAt: LocalDateTime
- **Relacionamentos**:
  - N:1 **user** (UserModel)
  - 1:N **comments** (CommentModel.post)
  - 1:N **likes** (LikeModel.post)

### CommentModel
- **Atributos**:
  - id: UUID (PK)
  - post: PostModel (FK, not null)
  - user: UserModel (FK, not null)
  - content: String (not null)
  - createdAt: LocalDateTime
- **Relacionamentos**:
  - N:1 **post** (PostModel)
  - N:1 **user** (UserModel)
  - 1:N **likes** (LikeModel.comment)

### LikeModel
- **Atributos**:
  - id: UUID (PK)
  - user: UserModel (FK, not null)
  - post: PostModel (FK, nullable)
  - comment: CommentModel (FK, nullable)
  - createdAt: LocalDateTime
- **Relacionamentos**:
  - N:1 **user** (UserModel)
  - N:1 **post** (PostModel)
  - N:1 **comment** (CommentModel)

### FriendshipModel
- **Atributos**:
  - id: UUID (PK)
  - sender: UserModel (FK, not null)
  - receiver: UserModel (FK, not null)
  - status: FriendshipStatus (enum)
  - createdAt: LocalDateTime
- **Relacionamentos**:
  - N:1 **sender** (UserModel)
  - N:1 **receiver** (UserModel)

### FollowModel
- **Atributos**:
  - id: UUID (PK)
  - follower: UserModel (FK)
  - followed: UserModel (FK)
- **Relacionamentos**:
  - N:1 **follower** (UserModel)
  - N:1 **followed** (UserModel)

### NotificationModel
- **Atributos**:
  - id: UUID (PK)
  - recipient: UserModel (FK, not null)
  - sender: UserModel (FK, not null)
  - type: NotificationType (enum)
  - title: String (not null)
  - message: String (not null)
  - referenceId: UUID
  - isRead: boolean
  - createdAt: LocalDateTime
- **Relacionamentos**:
  - N:1 **recipient** (UserModel)
  - N:1 **sender** (UserModel)

### EmailVerificationToken
- **Atributos**:
  - id: UUID (PK)
  - token: String
  - user: UserModel (FK)
  - expiryDate: LocalDateTime
- **Relacionamentos**:
  - 1:1 **user** (UserModel)

### PasswordResetToken
- **Atributos**:
  - id: Long (PK)
  - token: String
  - user: UserModel (FK, unique)
  - expiryDate: LocalDateTime
- **Relacionamentos**:
  - 1:1 **user** (UserModel)

## Descrição Textual do Diagrama MER

Abaixo está a descrição textual das ligações entre as entidades, com cardinalidades, chaves primárias (PK) e chaves estrangeiras (FK):

1. **UserModel**:
   - **PK**: id (UUID)
   - **FKs**: Nenhuma diretamente nos atributos.
   - **Relacionamentos**:
     - **1:N** com **FriendshipModel** (via `sender` e `receiver`): Um usuário pode enviar ou receber várias solicitações de amizade.
     - **1:N** com **PostModel** (via `user`): Um usuário pode criar vários posts.
     - **1:N** com **CommentModel** (via `user`): Um usuário pode criar vários comentários.
     - **1:N** com **LikeModel** (via `user`): Um usuário pode dar vários likes.
     - **1:N** com **FollowModel** (via `follower` e `followed`): Um usuário pode seguir vários outros e ser seguido por vários outros.
     - **1:N** com **NotificationModel** (via `sender` e `recipient`): Um usuário pode enviar ou receber várias notificações.
     - **1:1** com **EmailVerificationToken** (via `user`): Um usuário tem um token de verificação de e-mail.
     - **1:1** com **PasswordResetToken** (via `user`): Um usuário tem um token de redefinição de senha.

2. **PostModel**:
   - **PK**: id (UUID)
   - **FK**: user (UserModel, not null)
   - **Relacionamentos**:
     - **N:1** com **UserModel** (via `user`): Um post pertence a um único usuário.
     - **1:N** com **CommentModel** (via `post`): Um post pode ter vários comentários.
     - **1:N** com **LikeModel** (via `post`): Um post pode ter vários likes.

3. **CommentModel**:
   - **PK**: id (UUID)
   - **FKs**: post (PostModel, not null), user (UserModel, not null)
   - **Relacionamentos**:
     - **N:1** com **PostModel** (via `post`): Um comentário pertence a um único post.
     - **N:1** com **UserModel** (via `user`): Um comentário pertence a um único usuário.
     - **1:N** com **LikeModel** (via `comment`): Um comentário pode ter vários likes.

4. **LikeModel**:
   - **PK**: id (UUID)
   - **FKs**: user (UserModel, not null), post (PostModel, nullable), comment (CommentModel, nullable)
   - **Relacionamentos**:
     - **N:1** com **UserModel** (via `user`): Um like pertence a um único usuário.
     - **N:1** com **PostModel** (via `post`): Um like pode estar associado a um único post (se não for um comentário).
     - **N:1** com **CommentModel** (via `comment`): Um like pode estar associado a um único comentário (se não for um post).

5. **FriendshipModel**:
   - **PK**: id (UUID)
   - **FKs**: sender (UserModel, not null), receiver (UserModel, not null)
   - **Relacionamentos**:
     - **N:1** com **UserModel** (via `sender`): Uma solicitação de amizade é enviada por um único usuário.
     - **N:1** com **UserModel** (via `receiver`): Uma solicitação de amizade é recebida por um único usuário.

6. **FollowModel**:
   - **PK**: id (UUID)
   - **FKs**: follower (UserModel), followed (UserModel)
   - **Relacionamentos**:
     - **N:1** com **UserModel** (via `follower`): Um seguimento é feito por um único usuário.
     - **N:1** com **UserModel** (via `followed`): Um seguimento é direcionado a um único usuário.

7. **NotificationModel**:
   - **PK**: id (UUID)
   - **FKs**: recipient (UserModel, not null), sender (UserModel, not null)
   - **Relacionamentos**:
     - **N:1** com **UserModel** (via `recipient`): Uma notificação é recebida por um único usuário.
     - **N:1** com **UserModel** (via `sender`): Uma notificação é enviada por um único usuário.

8. **EmailVerificationToken**:
   - **PK**: id (UUID)
   - **FK**: user (UserModel)
   - **Relacionamentos**:
     - **1:1** com **UserModel** (via `user`): Um token de verificação está associado a um único usuário.

9. **PasswordResetToken**:
   - **PK**: id (Long)
   - **FK**: user (UserModel, unique)
   - **Relacionamentos**:
     - **1:1** com **UserModel** (via `user`): Um token de redefinição de senha está associado a um único usuário.

## Representação Textual do Diagrama

```
[UserModel]
  | id (PK), login, password, email, verifiedEmail, enabled, role, gender, dateOfBirth, profilePicture, profileDescription
  |
  |---1:N---> [FriendshipModel] (sender)
  |---1:N---> [FriendshipModel] (receiver)
  |---1:N---> [PostModel] (user)
  |---1:N---> [CommentModel] (user)
  |---1:N---> [LikeModel] (user)
  |---1:N---> [FollowModel] (follower)
  |---1:N---> [FollowModel] (followed)
  |---1:N---> [NotificationModel] (sender)
  |---1:N---> [NotificationModel] (recipient)
  |---1:1---> [EmailVerificationToken] (user)
  |---1:1---> [PasswordResetToken] (user)

[PostModel]
  | id (PK), user (FK), content, imageUrl, videoUrl, createdAt
  |
  |---N:1---> [UserModel] (user)
  |---1:N---> [CommentModel] (post)
  |---1:N---> [LikeModel] (post)

[CommentModel]
  | id (PK), post (FK), user (FK), content, createdAt
  |
  |---N:1---> [PostModel] (post)
  |---N:1---> [UserModel] (user)
  |---1:N---> [LikeModel] (comment)

[LikeModel]
  | id (PK), user (FK), post (FK, nullable), comment (FK, nullable), createdAt
  |
  |---N:1---> [UserModel] (user)
  |---N:1---> [PostModel] (post)
  |---N:1---> [CommentModel] (comment)

[FriendshipModel]
  | id (PK), sender (FK), receiver (FK), status, createdAt
  |
  |---N:1---> [UserModel] (sender)
  |---N:1---> [UserModel] (receiver)

[FollowModel]
  | id (PK), follower (FK), followed (FK)
  |
  |---N:1---> [UserModel] (follower)
  |---N:1---> [UserModel] (followed)

[NotificationModel]
  | id (PK), recipient (FK), sender (FK), type, title, message, referenceId, isRead, createdAt
  |
  |---N:1---> [UserModel] (recipient)
  |---N:1---> [UserModel] (sender)

[EmailVerificationToken]
  | id (PK), token, user (FK), expiryDate
  |
  |---1:1---> [UserModel] (user)

[PasswordResetToken]
  | id (PK), token, user (FK, unique), expiryDate
  |
  |---1:1---> [UserModel] (user)
```

## Observações
- O **LikeModel** possui chaves estrangeiras opcionais (`post` e `comment`), permitindo que um like seja associado a um post ou a um comentário, mas não a ambos simultaneamente.
- Os relacionamentos **FollowModel** e **FriendshipModel** modelam interações entre usuários, sendo essencialmente relações N:M implícitas (por meio de tabelas associativas).
- **EmailVerificationToken** e **PasswordResetToken** têm relacionamentos 1:1 com **UserModel**, garantindo unicidade para cada usuário.