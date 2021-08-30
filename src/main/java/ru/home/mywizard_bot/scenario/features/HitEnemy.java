package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.actions.Enemy;
import ru.home.mywizard_bot.utils.Emojis;

public class HitEnemy implements Feature {
    private String enemyId;

    public HitEnemy(String enemyId) {
        this.enemyId = enemyId;
    }

    @Override
    public void engage(UserProfileData profileData) {
        StringBuilder status = new StringBuilder();
        Integer enemyPower;
        Integer playerPower = profileData.getAttackPower();
        /*Enemy enemy = profileData.getEnemies().stream().filter(x -> x.getId().equals(enemyId)).findFirst().get();
        if (enemy.getAttackPower() > profileData.getAttackPower()) {
            profileData.setStrength(profileData.getStrength() - enemy.getDamage());
            status = profileData.getUserName() + " получает урон от " + enemy.getCaption().toUpperCase() + " " + enemy.getDamage() + " ед.\n\n";
        } else if (enemy.getAttackPower() < profileData.getAttackPower()) {
            enemy.setStrength(enemy.getStrength() - profileData.getDamage());
            status = enemy.getCaption().toUpperCase() + " получает урон " + enemy.getDamage() + " ед.\n\n";
        } else {
            status = "Вы парируете удар.\n\n";
        }*/

        for (Enemy enemy : profileData.getEnemies()) {
            if (enemy.getStrength() <= 0) continue;
            enemyPower = enemy.getAttackPower();
            if (enemy.getId().equals(enemyId) && enemyPower < playerPower) {
                enemy.setStrength(enemy.getStrength() - profileData.getDamage());
                status.append(String.format("%s (%d%s) наносит урон %d ед. %s (%d%s)\n\n",
                        profileData.getUserName(), playerPower, Emojis.DAGGER, enemy.getDamage(),
                        enemy.getCaption().toUpperCase(), enemyPower, Emojis.DAGGER));
            } else if (enemyPower > playerPower) {
                profileData.setStrength(profileData.getStrength() - enemy.getDamage());
                status.append(String.format("%s (%d%s) получает урон %d ед. от %s (%d%s)\n\n",
                        profileData.getUserName(), playerPower, Emojis.DAGGER, enemy.getDamage(),
                        enemy.getCaption().toUpperCase(), enemyPower, Emojis.DAGGER));
            } else if (enemy.getId().equals(enemyId) && enemyPower == playerPower) {
                status.append(String.format("%s (%d%s) парирует удар %s (%d%s)\n\n",
                        profileData.getUserName(), playerPower, Emojis.DAGGER,
                        enemy.getCaption().toUpperCase(), enemyPower, Emojis.DAGGER));
            } else {
                status.append(String.format("%s (%d%s) ускользает от атаки %s (%d%s)\n\n",
                        profileData.getUserName(), playerPower, Emojis.DAGGER,
                        enemy.getCaption().toUpperCase(), enemyPower, Emojis.DAGGER));
            }
        }
        profileData.setCombatStatus("*Ход " + profileData.getCombatTurn() + "*: _" + status + "_");
    }
}
