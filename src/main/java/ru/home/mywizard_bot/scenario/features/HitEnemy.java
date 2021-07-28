package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.actions.Enemy;

public class HitEnemy implements Feature {
    private String enemyId;

    public HitEnemy(String enemyId) {
        this.enemyId = enemyId;
    }

    @Override
    public void engage(UserProfileData profileData) {
        Enemy enemy = profileData.getEnemies().stream().filter(x -> x.getId().equals(enemyId)).findFirst().get();
        String status;
        if (enemy.getAttackPower() > profileData.getAttackPower()) {
            profileData.setStrength(profileData.getStrength() - enemy.getDamage());
            status = enemy.getCaption() + " наносит урон " + enemy.getDamage() + " ед.\n\n";
        } else if (enemy.getAttackPower() < profileData.getAttackPower()) {
            enemy.setStrength(enemy.getStrength() - profileData.getDamage());
            status = enemy.getCaption() + " получает урон " + enemy.getDamage() + " ед.\n\n";
        } else {
            status = "Вы парируете удар.\n\n";
        }
        profileData.setCombatStatus(status);
    }
}
