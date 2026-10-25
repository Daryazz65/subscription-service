CREATE TABLE user_subscription (
                                   user_login VARCHAR(255) PRIMARY KEY,
                                   subscription_type VARCHAR(16) NOT NULL,
                                   expires_at TIMESTAMPTZ,
                                   created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                   updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                   CONSTRAINT chk_user_subscription_type
                                       CHECK (
                                           (subscription_type = 'FREE' AND expires_at IS NULL)
                                               OR
                                           (subscription_type = 'PAID' AND expires_at IS NOT NULL)
                                           )
);